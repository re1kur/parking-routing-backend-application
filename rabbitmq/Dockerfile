FROM rabbitmq:latest

WORKDIR /etc/rabbitmq

COPY rabbitmq/definitions.json ./definitions.json
COPY rabbitmq/enabled_plugins ./enabled_plugins

ENV RABBITMQ_SERVER_ADDITIONAL_ERL_ARGS="-rabbitmq_management load_definitions '/etc/rabbitmq/definitions.json'"