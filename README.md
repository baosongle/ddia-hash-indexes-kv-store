# Hash indexes implementation

这个项目是对《设计数据密集型应用（影印版）》第 72 页的 Hash Indexes 章节所描述的算法的实现

我们要实现的是一个键值对数据库，提供 `set` 、`get`、`delete` 操作。

- 当 `set` 一个键值对时，将键值对追加到文件的末尾，并在内存中的使用 `Map` 保存这个键值对在文件中的偏移量
- 当 `get` 键值对时，从内存中的 `Map` 中获取到偏移量，并从文件中读取到值
- 当 `delete` 键值对时，相当于向文件末尾追加一个键值对，值是空字符串，并从内存中的 `Map` 删除键的偏移量

