package org.quickload.record;

import com.google.inject.Inject;
import org.junit.Ignore;

import java.util.Iterator;
import java.util.List;

@Ignore
public class TestRandomRecordGenerator
{
    private final RandomManager randomManager;

    @Inject
    public TestRandomRecordGenerator(RandomManager randomManager)
    {
        this.randomManager = randomManager;
    }

    public Iterable<Row> generate(final Schema schema, final int size)
    {
        return new Iterable<Row>() {
            public Iterator<Row> iterator() {
                return new Iterator<Row>() {
                    private int count;
                    @Override
                    public boolean hasNext()
                    {
                        return size > count;
                    }

                    @Override
                    public Row next()
                    {
                        count += 1;
                        return generateRow(schema.getColumns());
                    }

                    @Override
                    public void remove()
                    {
                        throw new AssertionError("NotSupported");
                    }
                };
            }
        };
    }

    private Row generateRow(List<Column> columns)
    {
        Object[] raw = new Object[columns.size()];
        for (int j = 0; j < raw.length; j++) {
            raw[j] = generateRecord(columns.get(j));
        }
        return new Row(raw);
    }

    private Object generateRecord(Column column)
    {
        Type type = column.getType();
        if (type.equals(LongType.LONG)) {
            return randomManager.getRandom().nextLong();
        } else if (type.equals(DoubleType.DOUBLE)) {
            return randomManager.getRandom().nextDouble();
        } else if (type.equals(StringType.STRING)) {
            return "muga"; // TODO
        } else {
            throw new AssertionError("NotSupportedType: " + column);
        }
    }
}
