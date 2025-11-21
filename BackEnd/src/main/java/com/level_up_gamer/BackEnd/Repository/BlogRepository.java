package com.level_up_gamer.BackEnd.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.level_up_gamer.BackEnd.Model.Blog.Blog;

@Repository
public interface BlogRepository extends JpaRepository<Blog, Long> {
    
}
