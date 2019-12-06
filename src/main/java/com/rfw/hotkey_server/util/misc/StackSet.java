/*
Copyright [yyyy] [name of copyright owner]

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */

package com.rfw.hotkey_server.util.misc;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Array;
import java.util.*;

/**
 * A Stack implementation with Set like O(log(n)) remove/insert
 *
 * - Supports LIFO push/pop/peek like stack (O(log(n)))
 * - Supports Set like insert/delete (any element) (O(log(n)))
 *
 * @author Raheeb Hassan
 */
public class StackSet<E> implements Set<E>, Queue<E> {
    private @Nonnegative int time = 0;

    /**
     * It uses two Maps where the key of one is the value of the other
     * stack is used to look up the largest key (last entry)
     * set is used to look up and delete random values
     */

    // insertTime -> value map
    private final TreeMap<Integer, E> stack = new TreeMap<>();
    // value -> insertTime map
    private final Map<E, Integer> set = new HashMap<>();

    @Override
    public int size() {
        return stack.size();
    }

    @Override
    public boolean isEmpty() {
        return stack.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return set.containsKey(o);
    }

    @Override
    public @Nonnull
    Iterator<E> iterator() {
        return new Iterator<E>() {
            // uses iterator of stack
            private Iterator<Map.Entry<Integer, E>> it = stack.entrySet().iterator();

            @Override
            public boolean hasNext() {
                return it.hasNext();
            }

            @Override
            public E next() {
                return it.next().getValue();
            }
        };
    }

    @Override
    public @Nonnull
    Object[] toArray() {
        return toArray(new Object[0]);
    }

    @SuppressWarnings("unchecked")
    @Override
    public @Nonnull
    <T> T[] toArray(T[] array) {
        int size = size();
        if (array.length < size) {
            // If array is too small, allocate the new one with the same component type
            array = (T[]) Array.newInstance(array.getClass().getComponentType(), size);
        } else if (array.length > size) {
            // If array is to large, set the first unassigned element to null
            array[size] = null;
        }
        int i = 0;
        for (E e: this) {
            // No need for checked cast - ArrayStoreException will be thrown
            // if types are incompatible, just as required
            array[i] = (T) e;
            i++;
        }
        return array;
    }

    @Override
    public boolean add(E e) {
        return offer(e);
    }

    public boolean addOrReplace(E e) {
        if (contains(e)) remove(e);
        return offer(e);
    }

    @Override
    public boolean remove(Object o) {
        if (contains(o)) {
            stack.remove(set.get(o));
            set.remove(o);
            if (isEmpty()) clear();
            return true;
        }
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c) {
            if (!contains(o)) return false;
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        if (c == null) throw new NullPointerException();
        boolean change = false;
        for (E e : c) {
            if (add(e)) change = true;
        }
        return change;
    }

    public boolean addOrReplaceAll(Collection<? extends E> c) {
        if (c == null) throw new NullPointerException();
        boolean change = false;
        for (E e : c) {
            if (addOrReplace(e)) change = true;
        }
        return change;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        if (c == null) throw new NullPointerException();
        boolean change = false;
        for (Object o : c) {
            if (remove(o)) change = true;
        }
        return change;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        if (c == null) throw new NullPointerException();
        List<E> self = new ArrayList<>(set.keySet());
        boolean change = false;
        for (E e : self) {
            if (!c.contains(e)) {
                if (remove(e)) change = true;
            }
        }
        return change;
    }

    @Override
    public void clear() {
        stack.clear();
        set.clear();
        time = 0;
    }

    @Override
    public boolean offer(E e) {
        if (e == null)
            throw new NullPointerException("Element can't be null");
        if (!contains(e)) {
            set.put(e, time);
            stack.put(time, e);
            time++;
            return true;
        }
        return false;
    }

    @Override
    public @Nonnull
    E remove() {
        E res = element();
        remove(res);
        return res;
    }

    @Override
    public @Nullable
    E poll() {
        E res = peek();
        if (res != null) remove(res);
        return res;
    }

    @Override
    public @Nonnull
    E element() throws NoSuchElementException {
        E res = peek();
        if (res != null) return res;
        throw new NoSuchElementException();
    }

    @Override
    public E peek() {
        if (isEmpty()) return null;
        return stack.lastEntry().getValue();
    }

    /* stack functions */

    public boolean push(E e) {
        return addOrReplace(e);
    }

    public boolean pushIfAbsent(E e) {
        return add(e);
    }

    public E pop() throws EmptyStackException {
        try {
            return remove();
        } catch (NoSuchElementException e) {
            throw new EmptyStackException();
        }
    }

    public E popIfPresent() {
        return poll();
    }

    public boolean empty() {
        return isEmpty();
    }

    @Override
    public String toString() {
        StringBuilder res = new StringBuilder("StackSet{");
        boolean start = true;
        for (E i: this) {
            if (!start) res.append(", ");
            else start = false;
            res.append(i);
        }
        return res.append("}").toString();
    }

    public static void main(String[] args) {
        StackSet<Integer> st = new StackSet<>();
        Stack<Integer> st2 = new Stack<>();

        int testNo = (int) 100;

        Random rand = new Random();
        long time = System.nanoTime();
        for (int i = 0; i < testNo; i++) {
            st.push(rand.nextInt());
        }
        System.out.println("StackSet.push " + (System.nanoTime() - time));

        time = System.nanoTime();
        for (int i = 0; i < testNo; i++) {
            st2.push(rand.nextInt());
        }
        System.out.println("Stack.push " + (System.nanoTime() - time));

//        time = System.nanoTime();
//        for (int i = 0; i < testNo; i++) {
//            st.popIfPresent();
//        }
//        System.out.println("StackSet.pop " + (System.nanoTime() - time));
//
//        time = System.nanoTime();
//        for (int i = 0; i < testNo; i++) {
//            st2.pop();
//        }
//        System.out.println("Stack.pop " + (System.nanoTime() - time));

        time = System.nanoTime();
        for (int i = 0; i < testNo; i++) {
            st.remove(rand.nextInt());
        }
        System.out.println("StackSet.remove " + (System.nanoTime() - time));

        time = System.nanoTime();
        for (int i = 0; i < testNo; i++) {
            st2.remove((Integer) rand.nextInt());
        }
        System.out.println("Stack.remove " + (System.nanoTime() - time));

    }
}
