package physics.geometry.linear;

import com.badlogic.gdx.math.Vector3;
import physics.components.Position;

/**
 * Created by Alexander on 25.05.2016.
 */
public class LineTranslator extends Line {

    /**
     * parametric constructor
     *
     * @param line vector to start point
     * @param position  vector to end point
     */
    public LineTranslator(Line line, Vector3 position)
    {
        super(new VertexTranslator(line.getStart(), position), new VertexTranslator(line.getEnd(), position));

    }
}
