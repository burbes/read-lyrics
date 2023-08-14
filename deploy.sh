mvn clean package dependency:copy-dependencies -DincludeScope=runtime -DskipTests
docker build --build-arg ENV_DOCKER=true --build-arg ENVIRONMENT=AWS -t read-lyric .
docker tag read-lyric:latest 742040112049.dkr.ecr.us-east-1.amazonaws.com/read-lyric:latest
docker push 742040112049.dkr.ecr.us-east-1.amazonaws.com/read-lyric:latest
