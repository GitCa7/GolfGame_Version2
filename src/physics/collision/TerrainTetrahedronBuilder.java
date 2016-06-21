package physics.collision;

import com.badlogic.gdx.math.Vector3;
import physics.geometry.planar.Triangle;
import physics.geometry.spatial.*;

/**
 * Class transforming terrain triangles into tetrahedra
 * created 16.06.16
 *
 * @author robin
 * @author martin
 */
public class TerrainTetrahedronBuilder
{
    /**
     * @param terrainMesh the terrain triangle mesh to build tetrahedra from
     */
    public TerrainTetrahedronBuilder(Triangle[] terrainMesh)
    {
        mTerrainMesh = terrainMesh;
    }

    /**
     * @param thickness the minimum thickness of the terrain
     * @return tetrahedra built from the terrain by projecting triangle vertices onto a plane below
     * the triangles such that the minimum thickness is conserved
     */
    public SolidTranslator[] build(float thickness)
    {
        //@TODO undo using one tetrahedron
        float bottomPlaneHeight = findLowestHeightValue() - thickness;

        //each triangle yields 3 tetrahedra
   //     SolidTranslator[] tetrahedra = new SolidTranslator[mTerrainMesh.length * 3];
        SolidTranslator[] tetrahedra = new SolidTranslator[mTerrainMesh.length];

        for (int cTriangle = 0; cTriangle < mTerrainMesh.length; ++cTriangle)
        {
            Triangle t = mTerrainMesh[cTriangle];
            //System.out.println("Number: " + i + "\t" + allTri[i]);
            //triangle vertices
            Vector3 a, b, c;
            a = t.getVertices()[0];
            b = t.getVertices()[1];
            c = t.getVertices()[2];


            //vertices translated to bottom plane y value, matching a, b, c in order
            Vector3 d, e, f;
            d = new Vector3((a.x + b.x + c.x) / 3, bottomPlaneHeight, (a.z + b.z + c.z) / 3);
/*            d = new Vector3(a.x, bottomPlaneHeight, a.z);
            e = new Vector3(b.x, bottomPlaneHeight, b.z);
            f = new Vector3(c.x, bottomPlaneHeight, c.z);
*/
/*
            //First tetrahedron ABCD with offset A
            tetrahedra[3*cTriangle] = makeAndPlaceTetrahedron(a, b, c, d);
            //Second tetrahedron CBFD
            tetrahedra[3*cTriangle + 1] = makeAndPlaceTetrahedron(c, b, d, f);
            //Third tetrahedron CDEF
            tetrahedra[3*cTriangle + 2] = makeAndPlaceTetrahedron(c, d, e, f);
 */
            tetrahedra[cTriangle] = makeAndPlaceTetrahedron(a, b, c, d);
        }
        return tetrahedra;
    }

    /**
     * @param a first vertex of tetrahedron
     * @param b second vertex of tetrahedron
     * @param c third vertex of tetrahedron
     * @param d fourth vertex of tetrahedron
     * @return a solid translator containing a tetrahedron with offset a
     */
    public SolidTranslator makeAndPlaceTetrahedron(Vector3 a, Vector3 b, Vector3 c, Vector3 d)
    {
        //norm b, c, d to origin
        TetrahedronParameter tetParam = new TetrahedronParameter(b.cpy().sub(a), c.cpy().sub(a), d.cpy().sub(a));
        Tetrahedron tet = TetrahedronPool.getInstance().getInstance(tetParam);
        return new SolidTranslator(tet, a.cpy());
    }

    /**
     * @return the lowest y coordinate value of any vertex of the triangle mesh stored
     */
    public float findLowestHeightValue()
    {
        float lowest = Float.MAX_VALUE;
        for (Triangle t : mTerrainMesh)
        {
            for (Vector3 v : t.getVertices())
            {
                if (v.y < lowest)
                    lowest = v.y;
            }
        }
        return lowest;
    }

    private Triangle[] mTerrainMesh;
}
