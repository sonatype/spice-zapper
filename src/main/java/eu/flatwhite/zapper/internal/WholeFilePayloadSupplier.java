package eu.flatwhite.zapper.internal;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import eu.flatwhite.zapper.IOSource;
import eu.flatwhite.zapper.Path;
import eu.flatwhite.zapper.ZFile;

/**
 * Payload supplier that simply "provides" whole files as is.
 * 
 * @author cstamas
 */
public class WholeFilePayloadSupplier
    implements PayloadSupplier
{
    private final IOSource ioSource;

    private final LinkedHashMap<Path, ZFile> zfiles;

    private final Iterator<Path> pathIterator;

    public WholeFilePayloadSupplier( final IOSource ioSource, final List<ZFile> zfiles )
    {
        this.ioSource = Check.notNull( ioSource, "IOSource is null!" );
        Check.argument( Check.notNull( zfiles, "ZFiles are null" ).size() > 0, "No ZFiles to supply!" );
        this.zfiles = new LinkedHashMap<Path, ZFile>( zfiles.size() );
        for ( ZFile zfile : zfiles )
        {
            this.zfiles.put( zfile.getIdentifier(), zfile );
        }
        this.pathIterator = this.zfiles.keySet().iterator();
    }

    @Override
    public synchronized Payload getNextPayload()
    {
        if ( pathIterator.hasNext() )
        {
            return getPayloadByIdentifier( pathIterator.next() );
        }
        else
        {
            return null;
        }
    }

    // ==

    protected Payload getPayloadByIdentifier( final Path identifier )
    {
        final ZFile zfile = zfiles.get( identifier );
        if ( zfile == null )
        {
            return null;
        }
        final Segment wholeFile = new Segment( zfile );
        return new SegmentPayload( identifier, wholeFile, ioSource );
    }
}
