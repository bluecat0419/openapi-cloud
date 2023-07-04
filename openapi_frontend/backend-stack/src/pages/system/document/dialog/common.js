export { default as OnEditor } from "./editor.vue"
export { default as OnIncrease } from "./increase.vue"
export { default as onPreview } from "./preview.vue"

export const wrap = "\n"
export function acceptImgType(File) {
  const fileType = File.type.split("/")[0]
  if (fileType === "image") {
    return true
  }
  return false
}
