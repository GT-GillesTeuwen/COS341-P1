public class OrderedPair<T, U> {

    private T obj1;
    private U obj2;

    OrderedPair(T obj1, U obj2) {
        this.obj1 = obj1;
        this.obj2 = obj2;
    }

    public T getObj1() {
        return obj1;
    }

    public U getObj2() {
        return obj2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OrderedPair<T, U> s = (OrderedPair<T, U>) o;
        return (obj1.equals(s.obj1) && obj2.equals(s.obj2));
    }

    @Override
    public int hashCode() {
        return obj1.hashCode() + obj2.hashCode();
    }

}
