# Use the base image with Java 11
FROM public.ecr.aws/lambda/java:11

# Declare a build argument
ARG ENVIRONMENT

# Create a directory to keep our app and its dependencies
WORKDIR /var/task

# Copy the compiled classes from your target directory into the container
COPY target/classes ${LAMBDA_TASK_ROOT}

# Copy the Maven dependencies into the container
COPY target/dependency/* ${LAMBDA_TASK_ROOT}/lib/

# Set the environment variable from the build argument
ENV ENVIRONMENT=$ENVIRONMENT

# Set the CMD to your handler
CMD [ "helloworld.App::handleRequest" ]
