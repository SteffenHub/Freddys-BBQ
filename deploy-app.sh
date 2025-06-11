#!/bin/bash

#
# Add this file to your server in a folder named 'app' if you deploy with gitlab CI/CD.
# It will be called by the CI/CD Gitlab Pipeline via ssh
#
# Provide an '.env' file in the same folder containing:
# MAIL_USERNAME="YOUR_MAIL_USERNAME"
# MAIL_PASSWORD="YOUR_MAIL_APP_PASSWORD"
#

set -ex # Stops the script on an error and shows the executed commands

cd "$(dirname "$0")"

# The Branch or tag which should be deployed
DEPLOY_TAG="$1"

echo "starting deployment for $DEPLOY_TAG"

echo "Validating environment configuration"

ENV_FILE="./.env"

echo 'Check if the env file exists'
if [ ! -f "$ENV_FILE" ]; then
  echo "ERROR: Could not found the .env file!"
  echo "Please create an .env file providing a MAIL_USERNAME and MAIL_PASSWORD"
  exit 1
fi

echo 'Check if the env file containing the needed environment variables'

# Export the environment variables from .env
set +x
export $(grep -v '^#' "$ENV_FILE" | xargs)

if [ -z "$MAIL_USERNAME" ] || [ -z "$MAIL_PASSWORD" ]; then
  echo "ERROR: One or more needed variables not set in the .env file."
  echo "Please provide MAIL_USERNAME and MAIL_PASSWORD in the .env file."
  exit 1
fi
set -x

echo "Configuration validation successful."

echo 'login to the gitlab docker registry'
docker login registry.gitlab.com -u <YOUR CI TOKEN USERNAME> -p <YOUR CI TOKEN>

echo "Pulling latest image..."
docker compose pull

echo "Stopping old and starting new container..."
docker compose up -d --remove-orphans

echo "Cleaning up old images..."
docker image prune -af

echo "Deployment successful!"