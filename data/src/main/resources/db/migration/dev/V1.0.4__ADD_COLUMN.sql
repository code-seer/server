ALTER TABLE public.user_profile ADD COLUMN IF NOT EXISTS avatar_object_key varchar(255);
ALTER TABLE public.user_profile ADD COLUMN IF NOT EXISTS language varchar(255);
