/**
 * base64 数据转 Blob
 * @param {*} base64Data base64的文件数据
 * @returns Blob
 */
export function base64ToBlob(base64Data) {
  const binaryString = atob(base64Data.split(",")[1])
  const type = base64Data.split(",")[0].match(/:(.*?);/)[1]
  const len = binaryString.length
  const bytes = new Uint8Array(len)

  for (let i = 0; i < len; i++) {
    bytes[i] = binaryString.charCodeAt(i)
  }

  return new Blob([bytes], { type })
}
