build:
	-rm -r dist/
	mkdir dist
	jspm bundle-sfx app/main dist/app.js
	./node_modules/.bin/uglifyjs dist/app.js -c -m -o dist/app.min.js