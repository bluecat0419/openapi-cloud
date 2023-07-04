import dayjs from "dayjs"
import duration from "dayjs/plugin/duration"
import "dayjs/locale/zh-cn"
import { useIntervalFn } from "@vueuse/core"

dayjs.locale("zh-cn")
dayjs.extend(duration)

const baseTime = baseMillisecondTime()

export function baseMillisecondTime() {
  return new Map([
    ["second", dayjs.duration(1, "second").asMilliseconds()],
    ["minute", dayjs.duration(1, "minute").asMilliseconds()],
    ["hour", dayjs.duration(1, "hour").asMilliseconds()],
    ["day", dayjs.duration(1, "day").asMilliseconds()],
    ["week", dayjs.duration(1, "week").asMilliseconds()],
    ["month", dayjs.duration(1, "month").asMilliseconds()],
    ["year", dayjs.duration(1, "year").asMilliseconds()],
  ])
}

export function parseSuffix(timer, timerType) {
  const periodTime = ["ss秒", "mm分", "HH时", "DD天", "MM月", "YY年"]
  let suffix = ""
  let periodTimeIndex = 0
  const totalTime = timer * baseTime.get(timerType)
  const baseTimeNumber = [...baseTime.values()]
  for (; periodTimeIndex < baseTimeNumber.length; periodTimeIndex++) {
    if (totalTime >= baseTimeNumber[periodTimeIndex]) {
      suffix = periodTime[periodTimeIndex].concat(suffix)
    } else {
      break
    }
  }
  return suffix
}
export function parseZhTime({ current, type }, suffix) {
  return dayjs.duration(current * baseTime.get(type)).format(suffix)
}

/**
 * 倒计时函数，接收时间，回调函数和时间段类型
 * @param {number} time 时间
 * @param {Function} callback 计时器中的回调函数
 * @param {string} timeType 时间段类型
 * @returns Object<Function>
 */
export function reciprocalTime(time, callback, timeType = "second") {
  let timing = time
  if (!baseTime.has(timeType)) {
    throw new Error("时间格式错误")
  }
  let suffix = parseSuffix(timing, timeType)
  let zh_time = parseZhTime({ current: timing, type: timeType }, suffix)

  const { pause, resume } = useIntervalFn(
    () => {
      --timing
      suffix = parseSuffix(timing, timeType)
      zh_time = parseZhTime({ current: timing, type: timeType }, suffix)
      if (timing <= 0) {
        pause()
        callback("0秒")
      } else {
        callback(zh_time)
      }
    },
    baseTime.get(timeType),
    false,
  )

  return {
    start() {
      resume()
    },
    stop() {
      pause()
    },
  }
}
