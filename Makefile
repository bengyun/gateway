all: docker

.PHONY: all docker

docker:
	docker build --tag=bengyun/gateway -f docker/Dockerfile .

