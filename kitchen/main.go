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
	"github.com/segmentio/kafka-go"
	"github.com/sethvargo/go-retry"
)

type Config struct {
	KafkaServers string `default:"localhost:29092"`
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

	slog.Info("connecting to kafka", "KafkaServers", config.KafkaServers)

	retrier := retry.NewFibonacci(3 * time.Second)
	retrier = retry.WithMaxDuration(60*time.Second, retrier)

	var conn *kafka.Reader
	err = retry.Do(context.Background(), retrier, func(ctx context.Context) error {
		conn = kafka.NewReader(
			kafka.ReaderConfig{
				Brokers: []string{config.KafkaServers},
				Topic:   "orders",
				GroupID: "kitchen",
				//				Partition: 0,
				MaxBytes: 10e6, // 10MB
			},
		)
		if err != nil {
			return retry.RetryableError(err)
		}

		slog.Info("Succesfully connected to topic orders")

		return nil
	})
	if err != nil {
		slog.Info("err connecting", "message", err)
		log.Fatal(err)
	}

	defer conn.Close()

	go func() {
		for {
			m, err := conn.ReadMessage(context.Background())
			if err != nil {
				break
			}
			fmt.Printf("message at offset %d: %s = %s\n", m.Offset, string(m.Key), string(m.Value))
		}
	}()

	port := "8070"
	slog.Info(fmt.Sprintf("Service kitchen cooking away on port %v ...", port))
	err = http.ListenAndServe(fmt.Sprintf(":%v", port), r)
	if err != nil {
		log.Fatal(err)
	}
}
