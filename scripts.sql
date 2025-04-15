-- Table: public.user_tasks

-- DROP TABLE IF EXISTS public.user_tasks;

CREATE TABLE IF NOT EXISTS public.user_tasks
(
    id uuid NOT NULL,
    workflow_instance_id uuid,
    step_id uuid,
    assigned_user text COLLATE pg_catalog."default",
    status text COLLATE pg_catalog."default",
    input_payload jsonb,
    output_payload jsonb,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    completed_at timestamp without time zone,
    CONSTRAINT user_tasks_pkey PRIMARY KEY (id),
    CONSTRAINT user_tasks_step_id_fkey FOREIGN KEY (step_id)
    REFERENCES public.workflow_steps (id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION,
    CONSTRAINT user_tasks_workflow_instance_id_fkey FOREIGN KEY (workflow_instance_id)
    REFERENCES public.workflow_instances (id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION,
    CONSTRAINT user_tasks_status_check CHECK (status = ANY (ARRAY['PENDING'::text, 'COMPLETED'::text, 'REJECTED'::text]))
    )

    TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.user_tasks
    OWNER to postgres;





-- Table: public.workflow_audit_logs

-- DROP TABLE IF EXISTS public.workflow_audit_logs;

CREATE TABLE IF NOT EXISTS public.workflow_audit_logs
(
    id uuid NOT NULL,
    workflow_instance_id uuid,
    step_id uuid,
    event text COLLATE pg_catalog."default",
    details jsonb,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT workflow_audit_logs_pkey PRIMARY KEY (id),
    CONSTRAINT workflow_audit_logs_step_id_fkey FOREIGN KEY (step_id)
    REFERENCES public.workflow_steps (id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION,
    CONSTRAINT workflow_audit_logs_workflow_instance_id_fkey FOREIGN KEY (workflow_instance_id)
    REFERENCES public.workflow_instances (id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION
    )

    TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.workflow_audit_logs
    OWNER to postgres;



-- Table: public.workflow_definitions

-- DROP TABLE IF EXISTS public.workflow_definitions;

CREATE TABLE IF NOT EXISTS public.workflow_definitions
(
    id uuid NOT NULL,
    name text COLLATE pg_catalog."default" NOT NULL,
    version integer NOT NULL,
    definition jsonb NOT NULL,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT workflow_definitions_pkey PRIMARY KEY (id)
    )

    TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.workflow_definitions
    OWNER to postgres;



-- Table: public.workflow_errors

-- DROP TABLE IF EXISTS public.workflow_errors;

CREATE TABLE IF NOT EXISTS public.workflow_errors
(
    id uuid NOT NULL,
    workflow_instance_id uuid,
    step_id uuid,
    error_message text COLLATE pg_catalog."default",
    stack_trace text COLLATE pg_catalog."default",
    retryable boolean DEFAULT true,
    occurred_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT workflow_errors_pkey PRIMARY KEY (id),
    CONSTRAINT workflow_errors_step_id_fkey FOREIGN KEY (step_id)
    REFERENCES public.workflow_steps (id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION,
    CONSTRAINT workflow_errors_workflow_instance_id_fkey FOREIGN KEY (workflow_instance_id)
    REFERENCES public.workflow_instances (id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION
    )

    TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.workflow_errors
    OWNER to postgres;





-- Table: public.workflow_instances

-- DROP TABLE IF EXISTS public.workflow_instances;

CREATE TABLE IF NOT EXISTS public.workflow_instances
(
    id uuid NOT NULL,
    workflow_definition_id uuid,
    status text COLLATE pg_catalog."default",
    current_step text COLLATE pg_catalog."default",
    input_data jsonb,
    output_data jsonb,
    started_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    completed_at timestamp without time zone,
    CONSTRAINT workflow_instances_pkey PRIMARY KEY (id),
    CONSTRAINT workflow_instances_workflow_definition_id_fkey FOREIGN KEY (workflow_definition_id)
    REFERENCES public.workflow_definitions (id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION,
    CONSTRAINT workflow_instances_status_check CHECK (status = ANY (ARRAY['PENDING'::text, 'RUNNING'::text, 'COMPLETED'::text, 'FAILED'::text]))
    )

    TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.workflow_instances
    OWNER to postgres;





-- Table: public.workflow_steps

-- DROP TABLE IF EXISTS public.workflow_steps;

CREATE TABLE IF NOT EXISTS public.workflow_steps
(
    id uuid NOT NULL,
    workflow_instance_id uuid,
    step_name text COLLATE pg_catalog."default",
    step_type text COLLATE pg_catalog."default",
    status text COLLATE pg_catalog."default",
    input_payload jsonb,
    output_payload jsonb,
    started_at timestamp without time zone,
    completed_at timestamp without time zone,
    retry_count integer DEFAULT 0,
    CONSTRAINT workflow_steps_pkey PRIMARY KEY (id),
    CONSTRAINT workflow_steps_workflow_instance_id_fkey FOREIGN KEY (workflow_instance_id)
    REFERENCES public.workflow_instances (id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION,
    CONSTRAINT workflow_steps_status_check CHECK (status = ANY (ARRAY['PENDING'::text, 'RUNNING'::text, 'COMPLETED'::text, 'FAILED'::text]))
    )

    TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.workflow_steps
    OWNER to postgres;
