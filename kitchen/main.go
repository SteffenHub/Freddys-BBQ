package main

import (
	"context"
	"fmt"
	"log"
	"log/slog"
	"net/http"
	"os"
	"time"

	"github.com/go-chi/chi/v5"
	"github.com/kelseyhightower/envconfig"
	amqp "github.com/rabbitmq/amqp091-go"
	"github.com/sethvargo/go-retry"
)

type Config struct {
	RabbitMQHost string `default:"localhost"`
}

func main() {
	logger := slog.New(slog.NewTextHandler(os.Stdout, nil))
	slog.SetDefault(logger)

	var config Config
	err := envconfig.Process("kitchen", &config)
	if err != nil {
		log.Fatal(err.Error())
	}

	r := chi.NewRouter()

	r.HandleFunc("GET /", func(w http.ResponseWriter, r *http.Request) {
		fmt.Fprintf(w, "Hello Kitchen")
	})

	rabbitMqConn := fmt.Sprintf("amqp://guest:guest@%s:5672/", config.RabbitMQHost)
	slog.Info("connecting to rabbit", "rabbitMqConn", rabbitMqConn)

	retrier := retry.NewFibonacci(3 * time.Second)
	retrier = retry.WithMaxDuration(60*time.Second, retrier)

	var conn *amqp.Connection
	var msgs <-chan amqp.Delivery
	err = retry.Do(context.Background(), retrier, func(ctx context.Context) error {
		conn, err = amqp.Dial(rabbitMqConn)
		if err != nil {
			return retry.RetryableError(err)
		}
		ch, err := conn.Channel()
		if err != nil {
			return retry.RetryableError(err)
		}

		slog.Info("Looking for queue kitchen.orders")

		msgs, err = ch.Consume(
			"kitchen.orders", // queue
			"",               // consumer
			true,             // auto-ack
			false,            // exclusive
			false,            // no-local
			false,            // no-wait
			nil,              // args
		)

		slog.Info("Not connected", err)

		if err != nil {
			return retry.RetryableError(err)
		}

		slog.Info("Succesfully connected to queue kitchen.orders")

		return nil
	})
	if err != nil {
		slog.Info("err connecting", "message", err)
		log.Fatal(err)
	}

	defer conn.Close()

	go func() {
		for d := range msgs {
			log.Printf("Received a message: %s", d.Body)
		}
	}()

	port := "8070"
	slog.Info(fmt.Sprintf("Service kitchen cooking away on port %v ...", port))
	err = http.ListenAndServe(fmt.Sprintf(":%v", port), r)
	if err != nil {
		log.Fatal(err)
	}
}
