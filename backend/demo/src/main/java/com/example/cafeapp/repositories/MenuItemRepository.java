    package com.example.cafeapp.repositories;

    import com.example.cafeapp.entities.Cafe;
    import com.example.cafeapp.entities.MenuItem;
    import org.springframework.data.jpa.repository.JpaRepository;

    import java.util.List;
    import java.util.Optional;

    public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {
        Optional<MenuItem> findByNameAndCafe(String name, Cafe cafe);

        List<MenuItem> findByCafeName(String cafeName);
    }