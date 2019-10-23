.PHONY: test

test:
	@echo "Running tests\n-------------"
	clj -A:test:runner

target/light.csv.jar:
	clj -A:depstar -m hf.depstar.jar target/light.csv.jar
