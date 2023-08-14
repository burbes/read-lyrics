mvn clean package dependency:copy-dependencies -DincludeScope=runtime -DskipTests
docker build --build-arg ENV_DOCKER=true --build-arg ENVIRONMENT=LOCALSTACK -t read-lyric .
sam build
sam local start-api
