#!/bin/bash

# setup ssh permissions to connect with deploy server
mkdir ~/.ssh/
DEPLOY_SSH_KEY_PRIVATE_FILE=~/.ssh/deploy_id_rsa
# Remove the previous file for sanity
rm ${DEPLOY_SSH_KEY_PRIVATE_FILE}
# Create new file with double line endings to ensure proper formatting
# on the CI configuration this text must not contain new lines at the end
echo -e "${DEPLOY_SSH_KEY_PRIVATE}\n\n" >> ${DEPLOY_SSH_KEY_PRIVATE_FILE}
# Make sure we are able to connect with the host without necessity of prompt
ssh-keyscan -H $DEPLOY_HOST >>~/.ssh/known_hosts
chmod 600 ${DEPLOY_SSH_KEY_PRIVATE_FILE}

DEPLOY_DIR=deploy_$DEPLOY_NAME
SSH_TARGET=${DEPLOY_USER}@${DEPLOY_HOST}

#echo "Logging in To The Registry On Remote Host"
#ssh -i ${DEPLOY_SSH_KEY_PRIVATE_FILE} ${SSH_TARGET} bash -c "'\
#  echo $DOCKER_REGISTRY_PASSWORD | docker login ${DOCKER_REGISTRY_HOST} -u ${DOCKER_REGISTRY_USERNAME} --password-stdin'"

echo "Putting Down Current Deployment"
ssh -i ${DEPLOY_SSH_KEY_PRIVATE_FILE} ${SSH_TARGET} bash -c "'cd ${DEPLOY_DIR} && docker-compose stop'"

echo "Creating Temporary Deployment Folder."
ssh -i ${DEPLOY_SSH_KEY_PRIVATE_FILE} ${SSH_TARGET} bash -c "'rm -rf ${DEPLOY_DIR} && mkdir -p ${DEPLOY_DIR}'"

echo "Copying Docker Compose File"
scp -i ${DEPLOY_SSH_KEY_PRIVATE_FILE} docker-compose.yml ${SSH_TARGET}:${DEPLOY_DIR}/docker-compose.yml

echo "Starting New Deployment"
ssh -i ${DEPLOY_SSH_KEY_PRIVATE_FILE} ${SSH_TARGET} bash -c "' \
  export DEPLOY_IMAGE=${DEPLOY_IMAGE} && \
  cd ${DEPLOY_DIR} && \
  docker-compose up -d'"
