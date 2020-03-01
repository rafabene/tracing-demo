const express = require('express')
const tracer = require('./tracer')
const middleware = require('express-opentracing').default
const app = express()
const port = 3000

app.use(middleware({tracer: tracer}))

app.get('/hello/:name', (req, res) => {
    console.log(req.headers)
    req.span.log({ event: 'hello', param: req.params.name })
    res.send('Microservice C: Hello ' + req.params.name + '!');
})


app.listen(port, () => {
    console.log(`Microservice C listening on port ${port}!`)
})