package eu.flatwhite.zapper.internal.segmenter;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import eu.flatwhite.zapper.Identifier;
import eu.flatwhite.zapper.ZFile;
import eu.flatwhite.zapper.ZFileRange;
import eu.flatwhite.zapper.internal.Check;

public class Recipe
{
    private final Identifier recipeIdentifier;

    private final Map<ZFile, List<ZFileRange>> participantRanges;

    private final Map<Identifier, List<ZFileRange>> tracks;

    public Recipe( final Identifier recipeIdentifier, final List<ZFile> participants,
                   final Map<ZFile, List<ZFileRange>> participantRanges, final Map<Identifier, List<ZFileRange>> tracks )
    {
        this.recipeIdentifier = Check.notNull( recipeIdentifier, "Recipe identifier is null!" );
        this.participantRanges =
            Collections.unmodifiableMap( Check.notNull( participantRanges, "Participant ranges map is null!" ) );
        this.tracks = Collections.unmodifiableMap( Check.notNull( tracks, "Tracks map is null!" ) );
    }

    public Identifier getRecipeIdentifier()
    {
        return recipeIdentifier;
    }

    public Map<ZFile, List<ZFileRange>> getParticipantRanges()
    {
        return participantRanges;
    }

    public Map<Identifier, List<ZFileRange>> getTracks()
    {
        return tracks;
    }
}
