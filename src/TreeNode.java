public class TreeNode {
    private AbstractExpression e;

    public TreeNode lNode;
    public TreeNode rNode;
    public TreeNode parent;

    public TreeNode() {
    }

    public void setE(AbstractExpression e) {
        this.e = e;
    }

    public AbstractExpression getE() {
        return e;
    }
}
