/**
 * mock 测试接口 示例
 * request 的 第二个参数 为 true 即可
 */
import request from "@/utils/request"

export function getTestInfo() {
  return request(
    {
      url: "/test",
      method: "get",
      headers: {
        isToken: false,
      },
    },
    true,
  )
}
