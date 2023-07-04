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
 * 扁平化数组方法
 */
export function flattenArray(arr) {
  return arr.reduce((result, item) => {
    if (Array.isArray(item)) {
      result.push(...flattenArray(item))
    } else {
      result.push(item)
    }
    return result
  }, [])
}

/**
 * 深拷贝函数方法，同时复制原型链上的属性和方法
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
