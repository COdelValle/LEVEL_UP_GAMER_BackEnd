package com.level_up_gamer.BackEnd.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.level_up_gamer.BackEnd.Model.Blog.Blog;

public interface BlogRepository extends JpaRepository<Blog, Long> {
    
}
