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

    // Guardar múltiples blogs:
    public List<Blog> saveAllBlogs(List<Blog> blogs){
        return repository.saveAll(blogs);
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

    // Incrementar vistas en +1 y devolver el blog actualizado
    public Blog incrementViews(Long id) {
        Blog b = getBlogByID(id);
        if (b == null) return null;
        Integer v = b.getViews();
        if (v == null) v = 0;
        b.setViews(v + 1);
        return repository.save(b);
    }

    // Incrementar likes en +1
    public Blog incrementLikes(Long id) {
        Blog b = getBlogByID(id);
        if (b == null) return null;
        Integer l = b.getLikes();
        if (l == null) l = 0;
        b.setLikes(l + 1);
        return repository.save(b);
    }

    // Decrementar likes en -1 (no menor a 0)
    public Blog decrementLikes(Long id) {
        Blog b = getBlogByID(id);
        if (b == null) return null;
        Integer l = b.getLikes();
        if (l == null || l <= 0) {
            b.setLikes(0);
        } else {
            b.setLikes(l - 1);
        }
        return repository.save(b);
    }
}

