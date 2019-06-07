# Headmaster

A classroom tracking tool for coding bootcamps.

## Development

### First-time Setup

Install [Node.js], then from the command line:

```sh
# installs node_modules/ folder
npm install
```

[Node.js]:https://nodejs.org/

### Backend

```sh
# Runs latest migrations and starts up the backend server.
# Backend server will reload with changes (uses nodemon) underneath.
npm start

# Run migrations on their own.
npm run migrate
```


### UI Development

```sh
# install shadow-cljs
npm install -g shadow-cljs

# watch CLJS files for changes; hosts a webserver at http://localhost:7772
shadow-cljs watch main

# produce CLJS build
shadow-cljs release main
```

## License

[ISC License](LICENSE.md)
