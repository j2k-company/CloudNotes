package site.j2k

interface ICollectionService <T> {
    fun getOne(id: Long): T
    fun getSeq(): List<T>
    fun create(model: T): Long
    fun update(id: Long, model: T): Boolean
    fun delete(id: Long): Boolean
}