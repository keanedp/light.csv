.PHONY: test

test:
	@echo "Running tests\n-------------"
	clj -A:test:runner
