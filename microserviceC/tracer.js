const os = require('os')
var { initTracerFromEnv, ZipkinB3TextMapCodec, opentracing } = require('jaeger-client')


// See schema https://github.com/jaegertracing/jaeger-client-node/blob/master/src/configuration.js#L37
var config = {
  serviceName: 'microserviceC',
}
var options = {
  logger: {
    info: function logInfo(msg) {
      console.log("INFO ", msg)
    },
    error: function logInfo(msg) {
      // Suppress errors
    }
  }
}
var tracer = initTracerFromEnv(config, options);

if (process.env.JAEGER_PROPAGATION == 'b3' ){
  console.log('Configured tracer to use \'b3\' propagation')
  let codec = new ZipkinB3TextMapCodec({ urlEncoding: true });
  
  tracer.registerInjector(opentracing.FORMAT_HTTP_HEADERS, codec);
  tracer.registerExtractor(opentracing.FORMAT_HTTP_HEADERS, codec);
}
  

module.exports = tracer