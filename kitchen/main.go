package main

import (
	"fmt"
	"github.com/go-chi/chi/v5"
	"io"
	"log"
	"log/slog"
	"net/http"
	"os"
)

func main() {
	logger := slog.New(slog.NewTextHandler(os.Stdout, nil))
	slog.SetDefault(logger)

	r := chi.NewRouter()

	r.HandleFunc("GET /", func(w http.ResponseWriter, r *http.Request) {
		fmt.Fprintf(w, "Hello Kitchen")
	})

	r.HandleFunc("POST /api/kitchen", func(w http.ResponseWriter, r *http.Request) {
		bodyBytes, _ := io.ReadAll(r.Body)
		requestBody := string(bodyBytes)
		slog.Info("Got it, we're on it ... %v", "body", requestBody)
		fmt.Fprintf(w, "Got it, we're on it ...")
	})

	port := "8070"
	slog.Info(fmt.Sprintf("Service kitchen cooking away on port %v ...", port))
	err := http.ListenAndServe(fmt.Sprintf(":%v", port), r)
	if err != nil {
		log.Fatal(err)
	}
}
