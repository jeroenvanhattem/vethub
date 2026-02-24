<script lang="ts">
	import { goto } from '$app/navigation';
	import { createAppointment } from '$lib/api/appointment/AppointmentController';
	import type { CreateAppointmentRequest } from '$lib/api/appointment/types';
	import { getPets } from '$lib/api/pet/PetController';
	import { getVets } from '$lib/api/vet/VetController';
	import type { PetResponse, VetResponse } from '$lib/api/models';
	import { Button } from '$lib/components/ui/button';
	import { Input } from '$lib/components/ui/input';
	import { Label } from '$lib/components/ui/label';
	import { Textarea } from '$lib/components/ui/textarea';
	import * as Select from '$lib/components/ui/select';
	import { Calendar, ArrowLeft, Save } from 'lucide-svelte';
	import { toast } from 'svelte-sonner';

	let pets = $state<PetResponse[]>([]);
	let vets = $state<VetResponse[]>([]);
	let loading = $state(true);
	let saving = $state(false);

	// Form state
	let selectedPetId = $state<number | undefined>();
	let selectedVetId = $state<number | undefined>();
	let scheduledDateTime = $state('');
	let reason = $state('');

	async function loadData() {
		loading = true;
		try {
			[pets, vets] = await Promise.all([getPets(), getVets()]);
		} catch (err) {
			toast.error('Failed to load pets or vets');
			console.error('Error loading data:', err);
		} finally {
			loading = false;
		}
	}

	async function handleSubmit(e: Event) {
		e.preventDefault();

		if (!selectedPetId || !selectedVetId || !scheduledDateTime || !reason.trim()) {
			toast.error('Please fill in all required fields');
			return;
		}

		saving = true;
		try {
			const request: CreateAppointmentRequest = {
				petId: selectedPetId,
				vetId: selectedVetId,
				scheduledDateTime,
				reason: reason.trim(),
				status: 'SCHEDULED'
			};

			await createAppointment(request);
			toast.success('Appointment created successfully');
			goto('/appointments');
		} catch (err) {
			toast.error('Failed to create appointment');
			console.error('Error creating appointment:', err);
		} finally {
			saving = false;
		}
	}

	// Format date for min attribute (current date/time)
	function getMinDateTime(): string {
		const now = new Date();
		const year = now.getFullYear();
		const month = String(now.getMonth() + 1).padStart(2, '0');
		const day = String(now.getDate()).padStart(2, '0');
		const hours = String(now.getHours()).padStart(2, '0');
		const minutes = String(now.getMinutes()).padStart(2, '0');
		return `${year}-${month}-${day}T${hours}:${minutes}`;
	}

	$effect(() => {
		loadData();
	});
</script>

<svelte:head>
	<title>New Appointment | VetHub</title>
</svelte:head>

<div class="container mx-auto px-4 py-8">
	<!-- Header -->
	<div class="mb-8 flex items-center justify-between">
		<div class="flex items-center gap-3">
			<div class="flex h-12 w-12 items-center justify-center rounded-lg bg-primary/10">
				<Calendar class="h-6 w-6 text-primary" />
			</div>
			<div>
				<h1 class="text-2xl font-bold text-foreground">Schedule New Appointment</h1>
				<p class="text-sm text-muted-foreground">Create a new appointment for a pet</p>
			</div>
		</div>
		<Button variant="outline" href="/appointments">
			<ArrowLeft class="mr-2 h-4 w-4" />
			Back
		</Button>
	</div>

	{#if loading}
		<div class="card p-12 text-center">
			<div class="mx-auto mb-4 h-8 w-8 animate-spin rounded-full border-4 border-primary border-t-transparent"></div>
			<p class="text-muted-foreground">Loading form data...</p>
		</div>
	{:else}
		<form onsubmit={handleSubmit} class="card p-6">
			<div class="space-y-6">
				<!-- Pet Selection -->
				<div class="space-y-2">
					<Label for="pet">Pet *</Label>
					<select
						id="pet"
						bind:value={selectedPetId}
						required
						class="flex h-10 w-full rounded-md border border-input bg-background px-3 py-2 text-sm ring-offset-background file:border-0 file:bg-transparent file:text-sm file:font-medium placeholder:text-muted-foreground focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2 disabled:cursor-not-allowed disabled:opacity-50"
					>
						<option value={undefined}>Select a pet...</option>
						{#each pets as pet (pet.id)}
							<option value={pet.id}>
								{pet.name} ({pet.type?.name ?? 'Unknown'})
							</option>
						{/each}
					</select>
					{#if pets.length === 0}
						<p class="text-xs text-muted-foreground">No pets available. Please add pets first.</p>
					{/if}
				</div>

				<!-- Vet Selection -->
				<div class="space-y-2">
					<Label for="vet">Veterinarian *</Label>
					<select
						id="vet"
						bind:value={selectedVetId}
						required
						class="flex h-10 w-full rounded-md border border-input bg-background px-3 py-2 text-sm ring-offset-background file:border-0 file:bg-transparent file:text-sm file:font-medium placeholder:text-muted-foreground focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2 disabled:cursor-not-allowed disabled:opacity-50"
					>
						<option value={undefined}>Select a veterinarian...</option>
						{#each vets as vet (vet.id)}
							<option value={vet.id}>
								{vet.firstName} {vet.lastName}
								{#if vet.specialties && vet.specialties.length > 0}
									- {vet.specialties.map(s => s.name).join(', ')}
								{/if}
							</option>
						{/each}
					</select>
					{#if vets.length === 0}
						<p class="text-xs text-muted-foreground">No vets available. Please add vets first.</p>
					{/if}
				</div>

				<!-- Date and Time -->
				<div class="space-y-2">
					<Label for="datetime">Scheduled Date & Time *</Label>
					<Input
						id="datetime"
						type="datetime-local"
						bind:value={scheduledDateTime}
						min={getMinDateTime()}
						required
					/>
					<p class="text-xs text-muted-foreground">Appointments must be scheduled in the future</p>
				</div>

				<!-- Reason -->
				<div class="space-y-2">
					<Label for="reason">Reason for Visit *</Label>
					<Textarea
						id="reason"
						bind:value={reason}
						placeholder="e.g., Annual checkup, vaccination, follow-up..."
						required
						rows={4}
					/>
				</div>

				<!-- Actions -->
				<div class="flex gap-3">
					<Button type="submit" disabled={saving || pets.length === 0 || vets.length === 0}>
						{#if saving}
							<div class="mr-2 h-4 w-4 animate-spin rounded-full border-2 border-primary-foreground border-t-transparent"></div>
							Creating...
						{:else}
							<Save class="mr-2 h-4 w-4" />
							Create Appointment
						{/if}
					</Button>
					<Button type="button" variant="outline" href="/appointments">
						Cancel
					</Button>
				</div>
			</div>
		</form>
	{/if}
</div>
