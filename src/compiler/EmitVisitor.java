package compiler;

import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import grammar.firstBaseVisitor;
import grammar.firstParser;

public class EmitVisitor extends firstBaseVisitor<ST> {
    private final STGroup stGroup;

    public EmitVisitor(STGroup group) {
        super();
        this.stGroup = group;
    }

    @Override
    protected ST defaultResult() {
        return stGroup.getInstanceOf("deflt");
    }

    @Override
    protected ST aggregateResult(ST aggregate, ST nextResult) {
        if(nextResult!=null)
            aggregate.add("elem",nextResult);
        return aggregate;
    }


    @Override
    public ST visitTerminal(TerminalNode node) {
        return new ST("Terminal node:<n>").add("n",node.getText());
    }

    @Override
    public ST visitAssign(firstParser.AssignContext ctx) {
        ST st = stGroup.getInstanceOf("assign");
        st.add("addres", ctx.ID().getText()).add( "value", visit(ctx.expr()));
        return st;
    }

    @Override
    public ST visitInt_tok(firstParser.Int_tokContext ctx) {
        ST st = stGroup.getInstanceOf("int");
        st.add("i",ctx.INT());
        return st;
    }

    @Override
    public ST visitId_tok(firstParser.Id_tokContext ctx) {
        ST st = stGroup.getInstanceOf("id");
        st.add("addres", ctx.ID());
        return st;
    }



    @Override
    public ST visitBinOp(firstParser.BinOpContext ctx) {
        ST st = null;
        switch (ctx.op.getType()) {
            case firstParser.ADD:
                st = stGroup.getInstanceOf("dodaj");
                break;
            case firstParser.SUB:
                st = stGroup.getInstanceOf("odejmij");
                break;
            case firstParser.MUL:
                st = stGroup.getInstanceOf("pomnoz");
                break;
            case firstParser.DIV:
                st = stGroup.getInstanceOf("podziel");
                break;
        default:
            throw new IllegalArgumentException("Nieobslugiwany operator: " + ctx.op.getText());
        }
        return st.add("p1", visit(ctx.l)).add("p2", visit(ctx.r));

    }
}
