package physics.generic;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Object maintaining a sequence of objects of set size sorted by time of addition.
 * If the buffer is full and a new element is added, the first element is removed and
 * the others moved towards the beginning to accomodate the lastest element.
 * created 20.06.16
 *
 * @author martin
 */
public class History<T>
{
    /**
     * @param size the maximum number of elements to store
     */
    public History(int size)
    {
        mPriorObjects = new LinkedList<>();
        mSize = size;
    }

    /**
     * @return the element added the latest
     * @throws IllegalStateException if no element was added after initialization or call to clear
     */
    public T getLast()
    {
        if (isEmpty())
            throw new IllegalStateException("no element was added to history");
        return mPriorObjects.get(mPriorObjects.size() - 1);
    }

    /**
     * @return the first element added
     * @throws IllegalStateException if no element was added after initialization or call to clear
     */
    public T getFirst()
    {
        if (isEmpty())
            throw new IllegalStateException("no element was added to history");
        return mPriorObjects.get(0);
    }

    /**
     * @param index index of element to retrieve
     * @return the element at index
     * @throws IllegalArgumentException if the index is beyond the number of elements stored
     */
    public T getElement(int index)
    {
        if (index >= mPriorObjects.size())
            throw new IllegalArgumentException("index beyond history scope");

        return mPriorObjects.get(index);
    }

    /**
     * @return the number of elements stored
     */
    public int getSize() { return mPriorObjects.size(); }

    /**
     * @return true if a new element can be added without removing another one
     */
    public boolean hasSpace()
    {
        return mPriorObjects.size() < mSize;
    }

    /**
     * @return if there are no elements stored
     */
    public boolean isEmpty()
    {
        return mPriorObjects.isEmpty();
    }

    /**
     * clears all elements stored
     */
    private void clear()
    {
        mPriorObjects.clear();
    }

    /**
     * adds last to the end of the queue and removes the head if necessary
     * @param last
     */
    public void push(T last)
    {
        if (!hasSpace())
            mPriorObjects.pollFirst();
        mPriorObjects.add(last);
    }

    private LinkedList<T> mPriorObjects;
    private int mSize;
}
