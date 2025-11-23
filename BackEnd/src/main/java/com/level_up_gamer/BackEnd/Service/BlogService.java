package com.level_up_gamer.BackEnd.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.level_up_gamer.BackEnd.Model.Blog.Blog;
import com.level_up_gamer.BackEnd.Repository.BlogRepository;

@Service
public class BlogService {
    @Autowired
    private BlogRepository repository;

    // Obtener info:
        // Obtener todas los Blogs:
        public List<Blog> getBlogs(){
            return repository.findAll();
        }

        // Obtener un Blog por el ID:
        public Blog getBlogByID(Long id){
            return repository.findById(id).orElse(null);
        }

        // Obtener varios Blogs por el autor(String):
        public List<Blog> getBlogByNombre(String autor){
            return repository.findByAuthor(autor);
        }
    
    // Guardar(agregar o actualizar Blog) Blog:
    public Blog saveBlog(Blog o){
        return repository.save(o);
    }

    // Eliminar Blog:
        // Por Objeto:
        public Boolean deleteBlog(Blog o){
            repository.delete(o);
            return !repository.existsById(o.getId());
        }

        // Por ID:
        public Boolean deleteBlogById(Long id){
            repository.deleteById(id);
            return !repository.existsById(id);
        }
}
