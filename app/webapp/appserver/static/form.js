function configValidator(value, config) {
  if (!value) {
    return [false, '此项为必填项']
  }
  return ''
}

define([], function () {
  return { configValidator };
});