package main

import (
	"context"
	"fmt"
	"io"
	"log"
	"log/slog"
	"net/http"
	"os"

	"github.com/go-chi/chi/v5"
	"github.com/riandyrn/otelchi"
	"go.opentelemetry.io/otel"
	"go.opentelemetry.io/otel/exporters/otlp/otlptrace"
	"go.opentelemetry.io/otel/exporters/otlp/otlptrace/otlptracehttp"
	"go.opentelemetry.io/otel/propagation"
	"go.opentelemetry.io/otel/sdk/resource"
	"go.opentelemetry.io/otel/sdk/trace"
	semconv "go.opentelemetry.io/otel/semconv/v1.4.0"
)

func main() {
	logger := slog.New(slog.NewJSONHandler(os.Stdout, &slog.HandlerOptions{
		ReplaceAttr: func(groups []string, a slog.Attr) slog.Attr {
			if a.Key == slog.MessageKey {
				a.Key = "message"
			} else if a.Key == slog.TimeKey {
				a.Key = "timestamp"
			}
			return a
		},
	}))
	slog.SetDefault(logger)

	// initialize trace provider
	tp, _ := newTraceProvider()
	defer func() {
		if err := tp.Shutdown(context.Background()); err != nil {
			log.Printf("Error shutting down tracer provider: %v", err)
		}
	}()

	// set global tracer provider & text propagators
	otel.SetTracerProvider(tp)
	otel.SetTextMapPropagator(propagation.NewCompositeTextMapPropagator(propagation.TraceContext{}, propagation.Baggage{}))
	// initialize tracer
	//	tracer = otel.Tracer("mux-server")

	r := chi.NewRouter()

	r.Use(otelchi.Middleware("kitchen", otelchi.WithChiRoutes(r)))

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

func newTraceProvider() (*trace.TracerProvider, error) {
	otelHost := "localhost"
	if os.Getenv("OTEL_HOST") != "" {
		otelHost = os.Getenv("OTEL_HOST")
	}

	opts := []otlptracehttp.Option{otlptracehttp.WithInsecure(), otlptracehttp.WithEndpointURL(fmt.Sprintf("http://%v:4318", otelHost))}

	traceExporter, err := otlptrace.New(
		context.Background(),
		otlptracehttp.NewClient(opts...),
	)
	if err != nil {
		return nil, err
	}

	/*
		traceProvider := trace.NewTracerProvider(
			trace.WithBatcher(traceExporter,
				// Default is 5s. Set to 1s for demonstrative purposes.
				trace.WithBatchTimeout(time.Second)),
		)
	*/
	res, err := resource.New(
		context.Background(),
		resource.WithAttributes(
			// the service name used to display traces in backends
			semconv.ServiceNameKey.String("kitchen"),
		),
	)
	if err != nil {
		log.Fatalf("unable to initialize resource due: %v", err)
	}
	return trace.NewTracerProvider(
		trace.WithSampler(trace.AlwaysSample()),
		trace.WithBatcher(traceExporter),
		trace.WithResource(res),
	), nil
}
