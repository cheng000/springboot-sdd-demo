# 2026-06-23
## model: 
  glm-5.2[1m]
## Agentic Coding: 
  Claude Code v2.0.75
## prompt: 
  帮我在这个目录下生成一个标准的java8 + springboot2.5的后端工程，采用controller + service + mapper 三层分层，包含一个CRUD接口内容，要求使用业界成熟且最推荐的全局异常处理，要求 在mapper返回给service，以及service返回给controller采用标准的返回类，要求三层之间的参数传递采用最合适的javabean（但是注意我不想在项目中维护过多的javabean），要求controller层只做请求接收、参数校验、权限校验等前置处理，service层只做业务逻辑处理，mapper层只做数据处理