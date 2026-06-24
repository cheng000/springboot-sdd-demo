package com.example.demo;

import com.example.demo.common.api.CommonResult;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.methods;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

/**
 * 架构守护测试（基于 ArchUnit + JUnit 5）
 * <p>
 * 作用：以测试的形式固化分层规则，每次 {@code mvn test} 都会自动执行，
 * 一旦有人写出违反分层的代码（例如 Controller 直接注入 Mapper、或 Controller 返回 PO），
 * 构建会立即失败，把问题拦截在 CI 阶段。
 * <p>
 * 维护方式：新增业务模块时无需修改本类，规则基于包名匹配自动生效。
 */
@AnalyzeClasses(packages = "com.example.demo")
public class ArchitectureTest {

    /**
     * 规则 1：Controller 不允许直接依赖 Mapper。
     * <p>
     * 业务背景：Controller 只能调用 Service，禁止跨层直接访问数据层。
     * 违反示例：{@code private final UserMapper userMapper;} 写在 Controller 中。
     */
    @ArchTest
    static final ArchRule controller_should_not_call_mapper =
            noClasses().that().resideInAPackage("..controller..")
                    .should().dependOnClassesThat().resideInAPackage("..mapper..")
                    .because("Controller 只允许调用 Service，禁止直接注入 / 调用 Mapper；请改用 XxxService");

    /**
     * 规则 2：Controller 中所有 public 方法的返回类型只能是 CommonResult。
     * <p>
     * 业务背景：保证对外响应结构统一，前端 / 联调方只需识别一种返回结构。
     * 违反示例：{@code public UserDTO getById(...)} 直接返回 DTO。
     */
    @ArchTest
    static final ArchRule controller_methods_should_return_common_result =
            methods()
                    .that().areDeclaredInClassesThat().resideInAPackage("..controller..")
                    .and().arePublic()
                    .should().haveRawReturnType(CommonResult.class)
                    .because("Controller 层统一返回 CommonResult，禁止直接返回 DTO/PO/Map/String 等其他类型");

    /**
     * 规则 3：Service 中所有 public 方法的返回类型只能是 CommonResult。
     * <p>
     * 业务背景：service 返回给 controller 的数据统一用 CommonResult 包装，
     * 这样 controller 不必再做包装，避免分层职责混乱。
     * 规则同时覆盖 service 接口与 impl 实现类。
     */
    @ArchTest
    static final ArchRule service_methods_should_return_common_result =
            methods()
                    .that().areDeclaredInClassesThat().resideInAPackage("..service..")
                    .and().arePublic()
                    .should().haveRawReturnType(CommonResult.class)
                    .because("Service 层统一返回 CommonResult 给 Controller；impl 中请用 @Override 实现接口方法");
}
