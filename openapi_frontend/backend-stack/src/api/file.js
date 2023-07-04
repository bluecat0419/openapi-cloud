import request from "@/utils/request.js"

/**
 * 上传图片，获得图床的图片地址
 * @param {*} file
 * @returns
 */
export function getUploadImgUrl(file, type = "blob") {
  const contentType = {
    blob: "multipart/form-data",
    base64: "application/x-www-form-urlencoded",
  }

  return request({
    url: "/file-service/upload",
    method: "post",
    headers: {
      "Content-Type": contentType[type],
    },
    data: file,
  })
}
