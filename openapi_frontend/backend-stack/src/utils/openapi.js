/**
 * 通用js方法封装处理
 * Copyright (c) 2023 openapi
 */

// 返回随机的一个uuid
export function uuid() {
  var temp_url = URL.createObjectURL(new Blob())
  var uuid = temp_url.toString()
  URL.revokeObjectURL(temp_url)
  return uuid.substr(uuid.lastIndexOf("/") + 1)
}

// 返回项目路径
export function getNormalPath(path) {
  if (path.length === 0 || !path || path === "undefined" || path === "null") {
    return path
  }
  let res = path.replace("//", "/")
  if (res.slice(-1) === "/") {
    return res.slice(0, -1)
  }
  return res
}

/**
 * 参数处理
 * @param {*} params  参数
 */
export function tansParams(params) {
  let result = ""
  for (const propName of Object.keys(params)) {
    const value = params[propName]
    var part = encodeURIComponent(propName) + "="
    if (value !== null && value !== "" && typeof value !== "undefined") {
      if (typeof value === "object") {
        for (const key of Object.keys(value)) {
          if (value[key] !== null && value[key] !== "" && typeof value[key] !== "undefined") {
            let params = propName + "[" + key + "]"
            var subPart = encodeURIComponent(params) + "="
            result += subPart + encodeURIComponent(value[key]) + "&"
          }
        }
      } else {
        result += part + encodeURIComponent(value) + "&"
      }
    }
  }
  return result
}

/**
 * 判断一个数据的类型
 */
export function classOf(o) {
  return Object.prototype.toString.call(0).slice(8, -1)
}

/**
 * 深拷贝方法 -- 包括复制对象的原型对象属性和方法
 * @param {Array | Object} obj
 * @returns {Array | Object}
 */
export function deepCopy(obj) {
  if (obj === null || typeof obj !== "object") {
    return obj
  }

  const copy = Array.isArray(obj) ? [] : {}

  for (const key in obj) {
    if (Object.prototype.hasOwnProperty.call(obj, key)) {
      if (typeof obj[key] === "function") {
        // 复制方法
        copy[key] = obj[key]
      } else {
        // 递归复制属性
        copy[key] = deepCopy(obj[key])
      }
    }
  }

  return copy
}

/**
 * 初始化对象的数据
 * @param {Array | Object} obj 初始化对象
 * @param { Any } init 初始化对象字段的初始值
 * @param {Object} initAttrs 初始化对象某些字段的初始值
 * @returns {Array | Object}
 */
export function initObjData(obj, init = "", initAttrs = {}) {
  let copyObj = JSON.parse(JSON.stringify(obj))
  const initAttrKeys = Object.keys(initAttrs)

  if (classOf(copyObj) === "Object") {
    // 键 和 值
    for (let [k, v] of Object.entries(copyObj)) {
      if (initAttrKeys.includes(k)) {
        copyObj[k] = initAttrs[k]
      } else if (classOf(v) === "Object" || classOf(v) === "Array") {
        copyObj[k] = initObj(v, init, initAttrs)
      } else {
        copyObj[k] = init
      }
    }
  } else if (classOf(copyObj) === "Array") {
    // 迭代器 索引 和 值
    for (let [i, v] of copyObj.entries()) {
      if (classOf(v) === "Object" || classOf(v) === "Array") {
        copyObj[i] = initObj(v, init, initAttrs)
      } else {
        copyObj[i] = init
      }
    }
  }

  return copyObj
}
