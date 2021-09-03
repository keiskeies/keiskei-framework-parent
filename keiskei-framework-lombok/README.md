# keiskei-framework-lombok

#### 介绍
基于jsr269, 父包[keiskei-framework-parent](https://gitee.com/morning-home/keiskei-framework-parent)

给图表类(带有@Chartable注解且父类中有指定field的类)生成指定参数的构造方法

```
public CLass(String index, Long indexNumber) {
    super.index = index;
    super.indexNumber = indexNumber;
}
```
### 目前存在问题
Windows idea自带预编译pre-compile会报错(似乎是在spring中和lombok冲突),建议使用maven compile

mac暂时未发现问题




