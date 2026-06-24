# SpringBoot SDD Demo Project

> 技术栈：Java 8 · SpringBoot 2.5.15 · 原生 MyBatis 2.2.2 · PostgreSQL · Lombok · MapStruct

## 一、工程结构

```
src/main/java/com/example/demo
├── DemoApplication.java            # 启动入口（@MapperScan）
├── common
│   ├── api
│   │   ├── CommonResult.java       # 统一返回类（service->controller、controller->前端）
│   │   └── ResultCode.java         # 统一状态码枚举
│   ├── exception
│   │   ├── BusinessException.java  # 业务异常
│   │   └── GlobalExceptionHandler.java # 全局异常处理（@RestControllerAdvice）
│   ├── page
│   │   ├── PageQuery.java          # 分页参数基类
│   │   └── PageResult.java         # 统一分页返回类
│   └── constant                    # 常量定义（按需扩展）
├── config
│   └── MybatisConfig.java          # 事务配置（@EnableTransactionManagement）
├── controller                      # 【控制层】请求接收 + 参数校验 + 权限校验
│   └── UserController.java
├── service                         # 【业务层】业务逻辑（接口 + impl）
│   ├── UserService.java
│   └── impl/UserServiceImpl.java
├── mapper                          # 【数据层】仅数据库读写
│   └── UserMapper.java
├── entity                          # PO（Persistent Object，与表一一对应）
│   └── User.java
├── dto                             # DTO/VO（请求接收 + 响应返回，两层策略）
│   ├── UserDTO.java                # 含 Create/Update 分组校验
│   └── UserQueryDTO.java           # 继承 PageQuery
└── convert
    └── UserConvert.java            # MapStruct 转换器（编译期生成）
```

## 二、分层职责（严格区分）

| 层        | 允许做的事                                                | 禁止做的事                          |
|-----------|----------------------------------------------------------|-------------------------------------|
| Controller| 请求接收、参数校验、权限校验、调用 Service              | 业务逻辑、直接调 Mapper             |
| Service   | 业务逻辑、调用 Mapper、PO/DTO 转换、抛业务异常          | 直接处理 HTTP，写 SQL               |
| Mapper    | 执行 SQL，返回 PO                                         | 业务逻辑、返回 DTO/VO               |

## 三、统一返回与异常处理

- **统一返回类** `CommonResult<T>`：包含 `code / message / data / timestamp`，并提供 `ok()/failed()` 等静态工厂方法。
- **Service 返回给 Controller 的就是 `CommonResult`**，因此 Controller 无需再包装。
- **全局异常处理**：`@RestControllerAdvice` + `@ExceptionHandler`，分别处理：
  - `BusinessException`：业务异常，返回业务码；
  - `MethodArgumentNotValidException` / `BindException` / `ConstraintViolationException`：参数校验；
  - `HttpMessageNotReadableException` / `MissingServletRequestParameterException` / `HttpRequestMethodNotSupportedException`：请求方法/参数异常；
  - `Throwable`：兜底 500，不暴露内部堆栈。

## 四、JavaBean 策略（PO + DTO/VO 两层）

- `entity/User`：PO，仅用于 Mapper 读写，不对外暴露。
- `dto/UserDTO`：同时作为新增/修改/单查响应的载体，通过 **JSR-303 分组校验**（`Create` / `Update`）让不同接口共用同一个 Bean。
- `dto/UserQueryDTO`：查询条件 + 分页参数，继承 `PageQuery`。
- `convert/UserConvert`：MapStruct 在编译期生成 `entity ↔ DTO` 转换代码，类型安全且零反射。

> 当业务变复杂导致单 DTO 字段冲突过多时，再拆分 `XxxCreateDTO` / `XxxUpdateDTO` / `XxxVO`，循序渐进。

## 五、环境与启动

### 1) 建库建表

```bash
psql -U postgres -d postgres -c "CREATE DATABASE demo_db;"
psql -U postgres -d demo_db -f src/main/resources/db/schema.sql
```

### 2) 修改连接信息

编辑 `src/main/resources/application.yml`（或 `application-dev.yml`）：

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/demo_db
    username: postgres
    password: 你的密码
```

### 3) 启动

```bash
mvn clean spring-boot:run
```

启动后访问 `http://localhost:8080/api`。

## 六、接口示例

| 方法   | 路径              | 说明         |
|--------|-------------------|--------------|
| POST   | /api/users        | 新增用户     |
| PUT    | /api/users        | 修改用户     |
| DELETE | /api/users/{id}   | 删除用户     |
| GET    | /api/users/{id}   | 查询单个用户 |
| GET    | /api/users        | 分页查询     |

### 请求/响应示例

新增：
```bash
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{"username":"dave","email":"dave@example.com","age":22,"phone":"13800000004"}'
```

```json
{
  "code": 20000,
  "message": "成功",
  "data": 4,
  "timestamp": 1718000000000
}
```

分页查询：
```bash
curl "http://localhost:8080/api/users?pageNum=1&pageSize=10&status=1"
```

参数校验失败响应：
```json
{
  "code": 40001,
  "message": "用户名不能为空; 邮箱格式不正确",
  "data": null,
  "timestamp": 1718000000000
}
```

业务异常响应（用户不存在）：
```json
{
  "code": 10001,
  "message": "用户不存在",
  "data": null,
  "timestamp": 1718000000000
}
```

## 七、Maven 打包

```bash
mvn clean package -DskipTests
java -jar target/springboot-mybatis-demo.jar
```
