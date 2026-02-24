<script lang="ts">
	import { getAppointments } from '$lib/api/appointment/AppointmentController';
	import type { AppointmentResponse, AppointmentStatus } from '$lib/api/appointment/types';
	import { Button } from '$lib/components/ui/button';
	import { Input } from '$lib/components/ui/input';
	import { Badge } from '$lib/components/ui/badge';
	import * as Table from '$lib/components/ui/table';
	import { Calendar, Plus, Search, PawPrint, User } from 'lucide-svelte';
	import { toast } from 'svelte-sonner';

	let appointments = $state<AppointmentResponse[]>([]);
	let loading = $state(true);
	let searchQuery = $state('');

	// Filtered appointments based on search query
	let filteredAppointments = $derived(() => {
		if (!searchQuery.trim()) return appointments;
		const query = searchQuery.toLowerCase();
		return appointments.filter(
			(appt) =>
				appt.petName?.toLowerCase().includes(query) ||
				appt.reason?.toLowerCase().includes(query) ||
				`${appt.vetFirstName} ${appt.vetLastName}`.toLowerCase().includes(query)
		);
	});

	async function loadAppointments() {
		loading = true;
		try {
			appointments = await getAppointments();
		} catch (err) {
			toast.error('Failed to load appointments');
			console.error('Error loading appointments:', err);
		} finally {
			loading = false;
		}
	}

	// Format date and time for display
	function formatDateTime(dateTimeString: string): string {
		const date = new Date(dateTimeString);
		return date.toLocaleString('en-US', { 
			year: 'numeric', 
			month: 'short', 
			day: 'numeric',
			hour: '2-digit',
			minute: '2-digit'
		});
	}

	// Get badge variant for status
	function getStatusVariant(status: AppointmentStatus): 'default' | 'secondary' | 'destructive' | 'outline' {
		switch (status) {
			case 'SCHEDULED': return 'secondary';
			case 'CONFIRMED': return 'default';
			case 'CANCELLED': return 'destructive';
			case 'COMPLETED': return 'outline';
			default: return 'secondary';
		}
	}

	// Load appointments on mount
	$effect(() => {
		loadAppointments();
	});
</script>

<svelte:head>
	<title>Appointments | VetHub</title>
</svelte:head>

<div class="container mx-auto px-4 py-8">
	<!-- Header -->
	<div class="mb-8 flex flex-col gap-4 sm:flex-row sm:items-center sm:justify-between">
		<div class="flex items-center gap-3">
			<div class="flex h-12 w-12 items-center justify-center rounded-lg bg-primary/10">
				<Calendar class="h-6 w-6 text-primary" />
			</div>
			<div>
				<h1 class="text-2xl font-bold text-foreground">Appointments</h1>
				<p class="text-sm text-muted-foreground">Manage appointments for pets and vets</p>
			</div>
		</div>
		<Button href="/appointments/new">
			<Plus class="mr-2 h-4 w-4" />
			New Appointment
		</Button>
	</div>

	<!-- Search -->
	<div class="mb-6">
		<div class="relative max-w-md">
			<Search class="absolute left-3 top-1/2 h-4 w-4 -translate-y-1/2 text-muted-foreground" />
			<Input
				type="search"
				placeholder="Search by pet, vet, or reason..."
				bind:value={searchQuery}
				class="pl-10"
			/>
		</div>
	</div>

	<!-- Table -->
	{#if loading}
		<div class="card p-12 text-center">
			<div class="mx-auto mb-4 h-8 w-8 animate-spin rounded-full border-4 border-primary border-t-transparent"></div>
			<p class="text-muted-foreground">Loading appointments...</p>
		</div>
	{:else if filteredAppointments().length === 0}
		<div class="card p-12 text-center">
			<Calendar class="mx-auto mb-4 h-12 w-12 text-muted-foreground/50" />
			{#if searchQuery}
				<p class="text-muted-foreground">No appointments found matching "{searchQuery}"</p>
			{:else}
				<p class="text-muted-foreground">No appointments scheduled yet</p>
				<Button href="/appointments/new" class="mt-4">
					<Plus class="mr-2 h-4 w-4" />
					Schedule First Appointment
				</Button>
			{/if}
		</div>
	{:else}
		<div class="card overflow-hidden">
			<Table.Root>
				<Table.Header>
					<Table.Row>
						<Table.Head>Date & Time</Table.Head>
						<Table.Head>Pet</Table.Head>
						<Table.Head>Vet</Table.Head>
						<Table.Head>Reason</Table.Head>
						<Table.Head>Status</Table.Head>
						<Table.Head class="w-[100px]">Actions</Table.Head>
					</Table.Row>
				</Table.Header>
				<Table.Body>
					{#each filteredAppointments() as appointment (appointment.id)}
						<Table.Row class="hover:bg-muted/50">
							<Table.Cell>
								<div class="flex items-center gap-2">
									<Calendar class="h-4 w-4 text-primary" />
									<span class="font-medium text-foreground">
										{formatDateTime(appointment.scheduledDateTime)}
									</span>
								</div>
							</Table.Cell>
							<Table.Cell>
								<div class="flex items-center gap-2 text-sm">
									<PawPrint class="h-3.5 w-3.5 text-muted-foreground" />
									{appointment.petName}
								</div>
							</Table.Cell>
							<Table.Cell>
								<div class="flex items-center gap-2 text-sm">
									<User class="h-3.5 w-3.5 text-muted-foreground" />
									{appointment.vetFirstName} {appointment.vetLastName}
								</div>
							</Table.Cell>
							<Table.Cell>
								<span class="text-sm text-muted-foreground">{appointment.reason}</span>
							</Table.Cell>
							<Table.Cell>
								<Badge variant={getStatusVariant(appointment.status)}>
									{appointment.status}
								</Badge>
							</Table.Cell>
							<Table.Cell>
								<Button variant="ghost" size="sm" href="/appointments/{appointment.id}">
									View
								</Button>
							</Table.Cell>
						</Table.Row>
					{/each}
				</Table.Body>
			</Table.Root>
		</div>
		<p class="mt-4 text-sm text-muted-foreground">
			Showing {filteredAppointments().length} of {appointments.length} appointments
		</p>
	{/if}
</div>
