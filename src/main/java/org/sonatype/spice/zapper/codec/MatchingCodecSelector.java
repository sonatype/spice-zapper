package org.sonatype.spice.zapper.codec;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.sonatype.spice.zapper.CodecSelector;
import org.sonatype.spice.zapper.ZFile;

import static com.google.common.base.Preconditions.checkNotNull;

public class MatchingCodecSelector
    implements CodecSelector
{
    private final Map<Pattern, Codec> codecs;

    public MatchingCodecSelector()
    {
        this( new LinkedHashMap<Pattern, Codec>() );
    }

    public MatchingCodecSelector( final Map<Pattern, Codec> codecs )
    {
        this.codecs = Collections.unmodifiableMap( codecs );
    }

    public List<Codec> selectCodecs( ZFile zfile )
    {
        if ( codecs.isEmpty() )
        {
            return Collections.emptyList();
        }

        final ArrayList<Codec> result = new ArrayList<Codec>();
        for ( Map.Entry<Pattern, Codec> entry : codecs.entrySet() )
        {
            if ( entry.getKey().matcher( zfile.getIdentifier().stringValue() ).matches() )
            {
                result.add( entry.getValue() );
            }
        }
        return result;
    }

    // ==

    public static MatchingCodecSelectorBuilder builder()
    {
        return new MatchingCodecSelectorBuilder();
    }

    // ==

    public static class MatchingCodecSelectorBuilder
    {
        private final LinkedHashMap<Pattern, Codec> codecs;

        public MatchingCodecSelectorBuilder()
        {
            this.codecs = new LinkedHashMap<Pattern, Codec>();
        }

        public MatchingCodecSelectorBuilder add( final String regex, final Codec codec )
        {
            checkNotNull(regex);
            checkNotNull(codec);
            return add( Pattern.compile( regex ), codec );
        }

        public MatchingCodecSelectorBuilder add( final Pattern regex, final Codec codec )
        {
            checkNotNull(regex);
            checkNotNull(codec);
            codecs.put( regex, codec );
            return this;
        }

        public MatchingCodecSelector build()
        {
            return new MatchingCodecSelector( codecs );
        }
    }
}
