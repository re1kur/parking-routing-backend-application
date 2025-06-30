FROM rabbitmq:latest

WORKDIR /etc/rabbitmq

COPY ./definitions.json ./definitions.json
COPY ./enabled_plugins ./enabled_plugins

ENV RABBITMQ_SERVER_ADDITIONAL_ERL_ARGS="-rabbitmq_management load_definitions '/etc/rabbitmq/definitions.json'"