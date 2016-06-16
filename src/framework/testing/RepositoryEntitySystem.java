package framework.testing;

import physics.collision.CollisionRepository;

/**
 * Interface for classes requiring a collision repository to work
 * created 16.06.16
 *
 * @author martin
 */
public interface RepositoryEntitySystem
{
    public void setRepository(CollisionRepository repo);
}
