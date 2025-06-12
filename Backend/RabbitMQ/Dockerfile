FROM rabbitmq:3.13-management

COPY rabbitmq.conf /etc/rabbitmq/rabbitmq.conf

#COPY definitions.json /etc/rabbitmq/definitions.json

EXPOSE 5672
EXPOSE 15672

HEALTHCHECK CMD rabbitmq-diagnostics -q ping

# CMD ["rabbitmq-server"]