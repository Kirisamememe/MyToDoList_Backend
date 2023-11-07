package com.example.mytodolist_backend.repository;

import com.example.mytodolist_backend.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByStatusId(Long id);
    List<Task> findByPriorityId(Long id);
    List<Task> findByCategoryId(Long id);

    @Query(value = "SELECT * FROM Task WHERE YEAR(due_date) = :year", nativeQuery = true)
    List<Task> findByYear(@Param("year") Integer year);

    @Query(value = "SELECT * FROM Task WHERE YEAR(due_date) = :year AND MONTH(due_date) = :month", nativeQuery = true)
    List<Task> findByYearAndMonth(@Param("year") Integer year, @Param("month") Integer month);
}


//save(S entity): 与えられたエンティティを保存し、その結果を返します。

//saveAll(Iterable<S> entities): 与えられたエンティティの集合を保存します。

//findById(ID id): 指定されたIDで識別されるエンティティを取得します。
//existsById(ID id): 指定されたIDのエンティティが存在するかどうかを返します。

//findAll(): すべてのエンティティを取得します。
//findAll(Sort sort): すべてのエンティティを指定されたソート順で取得します。
//findAllById(Iterable<ID> ids): 指定されたIDの集合に該当するすべてのエンティティを取得します。

//getOne(ID id): 参照として指定されたIDのエンティティを返します（遅延ロード）。

//deleteById(ID id): 指定されたIDのエンティティを削除します。
//delete(S entity): 与えられたエンティティを削除します。
//deleteAll(Iterable<? extends T> entities): 与えられたエンティティの集合を削除します。
//deleteAll(): すべてのエンティティを削除します。
//deleteAllInBatch(): 一括で（バッチで）すべてのエンティティを削除します。
//deleteInBatch(Iterable<T> entities): 一括で（バッチで）指定されたエンティティを削除します。

//count(): エンティティの総数を返します。

//findAll(Pageable pageable): ページネーション情報に基づいてエンティティを取得します。

//flush(): 永続化コンテキストをフラッシュして現在のトランザクションに書き込みます。

//saveAndFlush(S entity): エンティティを保存してすぐにフラッシュ（永続化）します。