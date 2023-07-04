//此处的规则供参考，其中多半其实都是默认值，可以根据个人习惯改写
module.exports = {
  //单行长度
  printWidth: 100,
  //缩进长度
  tabWidth: 2,
  //使用空格代替tab缩进
  useTabs: false,
  //句末使用分号
  semi: false,
  //使用单引号
  singleQuote: false,
  //仅在必需时为对象的key添加引号
  quoteProps: "as-needed",
  // jsx中使用单引号
  jsxSingleQuote: true,
  //多行时尽可能打印尾随逗号
  trailingComma: "all",
  //在对象前后添加空格-eg: { foo: bar }
  bracketSpacing: true,
  //多属性html标签的‘>’折行放置
  jsxBracketSameLine: true,
  //单参数箭头函数参数周围使用圆括号-eg: (x) => x
  arrowParens: "always",
  // 如果超过打印宽度，则换行。
  proseWrap: "always",
  //无需顶部注释即可格式化
  requirePragma: false,
  //在已被preitter格式化的文件顶部加上标注
  insertPragma: false,
  proseWrap: "preserve", //不知道怎么翻译
  //对HTML全局空白不敏感
  htmlWhitespaceSensitivity: "ignore",
  //不对vue中的script及style标签缩进
  vueIndentScriptAndStyle: false,
  //结束行形式
  endOfLine: "lf",
  //对引用代码进行格式化
  embeddedLanguageFormatting: "auto",
}
