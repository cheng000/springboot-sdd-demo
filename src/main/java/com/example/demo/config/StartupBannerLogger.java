package com.example.demo.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * 应用启动后日志输出器
 * <p>
 * 在 {@link ApplicationReadyEvent} 触发时（所有 Bean 初始化完成、Web 服务器已监听端口），
 * 打印 Knife4j 文档地址、Swagger UI 地址、API JSON 地址，方便启动后直接点开。
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class StartupBannerLogger {

    private final Environment env;

    @EventListener(ApplicationReadyEvent.class)
    public void printApiUrls() {
        String port = env.getProperty("server.port", "8080");
        String contextPath = env.getProperty("server.servlet.context-path", "");
        // 去掉末尾斜杠，避免双斜杠
        if (contextPath.endsWith("/")) {
            contextPath = contextPath.substring(0, contextPath.length() - 1);
        }
        String base = "http://localhost:" + port + contextPath;

        StringBuilder sb = new StringBuilder("\n");
        sb.append("\n================ 接口文档已就绪 ================\n");
        sb.append(" Knife4j 增强文档  : ").append(base).append("/doc.html\n");
        sb.append(" Swagger UI       : ").append(base).append("/swagger-ui.html\n");
        sb.append(" OpenAPI JSON     : ").append(base).append("/v3/api-docs\n");
        sb.append("------------------------------------------------\n");
        sb.append(" 已自动生成接口列表， Knife4j 调试 Tab 已预填参数，直接点击「发送」即可\n");
        sb.append("================================================\n");
        log.info(sb.toString());
    }
}
