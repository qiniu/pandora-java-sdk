 function methodValidator(methods) {
     const methodsArr = Object.keys(methods).map(key => {
        return methods[key].active
      })
      if (methodsArr.every(item => !item)) {
        return [false, '此项为必填项']
      }
    return ''
}
  
define([], function() {
    return {methodValidator};
});